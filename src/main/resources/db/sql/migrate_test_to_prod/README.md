# Migrating Test Server Data to Production Server Data
Apart from optimizations for specific use cases, there's a major difference between the
BrAPI Java test server and the production server:

**All id columns are replaced with the UUID data type instead of TEXT in the production database.**

This change was done out of need for:
1. Standardization of good data practices
2. Performance Optimization

The performance difference between TEXT and UUID columns in the database might not be felt in use cases
where there are small amounts of data, but in large batch operations this optimization can speed queries up by about double time.

This database schema change doesn't only affect the DB of BrAPI Production Server, as the codebase has also been modified to
accommodate for this data standardization.

This document will help you prepare for a data migration to using UUIDs the standard ID column type.

## Do you really need to migrate test server data?
Here at BrAPI we hope that you used the test server for non-production data for your application.  Since the introduction of
BrAPI Java Production Server we hope that the test server is only used for testing for your application before you go live.

If that's the case, you should need to do nothing to proceed.  Simply build the application on an empty DB and the schema
should be generated with UUID as the ID column type.

However, since the production server was only recently introduced, we realize this is likely not the case.

If you have been utilizing the test server with production data, there are several steps you will need to take to swap over
to the production data model.

This document will cover these.

## Step 1: Undo Dummy Migration Data
In the BrAPI Java Test Server, there are some migration scripts kicked off the first time
you build the app to put in some data you can look at and query to understand how the data model works.

Unfortunately a lot of this data uses non-UUIDs for the identifiers of the dummy data that is being inserted.

As such, you will need to remove this dummy data from your database in order to proceed with the migration.

To do this, find the `undo_dummy_data` folder and go one by one and copy and paste all the undo migration scripts in order
into the SQL execution service of your choice to remove all of this data from the database.

## Step 2: Validate id columns
After removing the dummy data, you will next want to check if there still exists non-UUID data in other id columns.

This should only be possible via other migration scripts your application has applied that included invalid UUID data, as
the BrAPI test server does create UUIDs by default.

To do this validation, you need to create a stored procedure we have written in your DB instance that you can run to verify.

This script is found in the `validate_id_columns.sql` file provided in this directory.

There are two notable omissions of id columns that are not validated in this script:
* `external_reference.external_reference_id` which is in fact not a UUID column as defined by the production server spec.  This is because this ID is supposed to be flexible to whatever id the client sends.
* `table.auth_user_id` This ID column is already a known issue, and will be resolved in another step.  This is because by default the test server inserts `anonymousUser` as the default `auth_user_id` when one isn't sent in the request. More info on the  `auth_user_id` section.

Once you have run the validation script, it should tell you any tables and their associated id columns that have invalid UUIDs in their id columns.

If such columns were found, you can run `retrieve_table_data_with_invalid_id_cols.sql` for any of the columns that have invalid data
to grab the bad data.  If the data doesn't fall under any of the steps outlined in this document, you will have to resolve this on your own or you can reach out to someone at the BrAPI team.

It's likely that once offending data is found, the data is referenced as foreign keys in other tables.

If that is the case, and the data (and its associated references) can't just be removed you will have to go through the process of inserting a new row (with a correctly generated UUID) and reassigning the foreign keys pointing to the old id
to the new one.  An example has been done for you via `migrate_crops.sql`. There may be a way to iterate through all the IDs, but hopefully 
the amount of data you have is small enough you can do it one by one.

## Step 3: Dump the database

At this point after you have validated the schema to be rid of non-UUID id data (save `auth_user_id`, more on that soon),
you are ready to do a pg_dump.

You can accomplish this with the following command:

`pg_dump -U db_username -d db_name --data-only > dump.sql`

where `db_username` is the username you can login with to your database, and `db_name` is the name of the database you want to dump.

This command will grab only the data associated with each table.  It will not copy the schema. It will place the results in a `dump.sql`
file in the directory you ran the command from.

If your database exists in a docker container, the command will look something like this:

`docker exec db_container_name pg_dump -U db_username -d db_name --data-only > dump.sql`

To play it totally safe, let's also grab a copy of the data and the database schema together in the event something goes awry
in the next steps.

To do this, create another dump using:

`pg_dump -U db_username -d db_name > dump_with_schema.sql`

Or with Docker:

`docker exec db_container_name pg_dump -U db_username -d db_name > dump_with_schema.sql`

In the event that you somehow lose the original database, you can reload it by creating the database and simply loading the 
`dump_with_schema.sql` file onto it. (More on that later).

## Step 4: Modify the dumpfile

**NOTE: We are talking about the `dump.sql` file without the schema here.  The schema file we created in step 3 should remain unchanged.**

As stated previously, we've largely been ignoring all the `anonymousUser`-filled `auth_user_id` columns.  These do in fact need
to become UUIDs, and for that, we need to line them up with the new expected default UUID for that column `'AAAAAAAA-AAAA-AAAA-AAAA-AAAAAAAAAAAA'`.

Iterating through every table and modifying this column would take too long, so instead just run a find and replace on the exported dump file
in your text editor of choice.  If you have a large amount of data, this operation might be too much for that text editor.  In which case you
likely would have to edit the database for these columns before exporting.

## Step 5: Create and load the new database

Now that the dumpfile has been modified, we are ready to load the new database.
To do this, use `psql` to login to the postgresql server, wherever it is hosted.

If your database is hosted on a docker container, this looks like:

`docker exec -it name_of_db_container psql -U db_username db_name`

Once in the `psql` CLI, you need to create your new database.  Call it something like:

`CREATE DATABASE db_name_uuid`

Eventually, if you want to keep your old database name you can rename this database after the old one is removed.

Next, find the `application.properties` file of the BrAPI server code and change the `spring.datasource.url` so that it points to the correct database, the new one we just created.

Now, build the server application like normal with `mvn clean install`

Then run the application like normal:

`java '-agentlib:jdwp=transport=dt_socket,server=y,suspend=n,address=localhost:5006' -jar target/brapi-Java-TestServer*.jar`

This should trigger flyway to create the database using the initial schema, which was modified for the production server to 
generate the database with UUID for all `id` columns and their associated foreign keys.

You can verify this was successful by checking the `psql` CLI to see if there were any tables created with

`\dt`

and you can further check that the schema was created with UUID type `id` columns by picking a table and running the table description command, like

`\d program`

which should look something like:

```
                     Table "public.program"
       Column        |  Type   | Collation | Nullable | Default 
---------------------+---------+-----------+----------+---------
 id                  | uuid    |           | not null | 
 additional_info     | jsonb   |           |          | 
 auth_user_id        | uuid    |           |          | 
 abbreviation        | text    |           |          | 
 documentationurl    | text    |           |          | 
 funding_information | text    |           |          | 
 name                | text    |           |          | 
 objective           | text    |           |          | 
 program_type        | integer |           |          | 
 crop_id             | uuid    |           |          | 
 lead_person_id      | uuid    |           |          | 
```

Now that the database has been created, all that's left it to load the dump file we have into it.

To do this, give `psql` the file as input to load in an import with:

`psql -U db_username db_name_uuid < dump.sql`

For docker, because the file isn't hosted there, you need to pipe it using `cat` like:

`cat dump.sql | docker exec -i name_of_db_container psql -U db_username db_name_uuid `

This should kick off copy statements for every table.  Ensure that there aren't errors.

If an error happens for any of the copy statements, the entire table it was trying to copy will not work.

An error on the `flyway_schema_history` is expected in most cases and is not a worry.  You really only want the new flyway schema table created by the migrations
run the first time you ran the app.

If the errors happened on other tables, you might have to do some sleuthing to figure out why and run this step again.

## Congrats!

With this, you should have successfully migrated the test server DB to the production DB.