-- See the NOTICE file distributed with this work for additional information
-- regarding copyright ownership.
--
-- Licensed under the Apache License, Version 2.0 (the "License");
-- you may not use this file except in compliance with the License.
-- You may obtain a copy of the License at
--
--     http://www.apache.org/licenses/LICENSE-2.0
--
-- Unless required by applicable law or agreed to in writing, software
-- distributed under the License is distributed on an "AS IS" BASIS,
-- WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
-- See the License for the specific language governing permissions and
-- limitations under the License.

-- change client_plate_db_id back to text instead of forcing uuid
ALTER TABLE vendor_order
ALTER COLUMN client_plate_db_id
    TYPE text
    USING client_plate_db_id::text;

-- change client_id back to text instead of forcing uuid
ALTER TABLE plate_submission
ALTER COLUMN client_id
    TYPE text
    USING client_id::text;

-- change client_plate_db_id back to text instead of forcing uuid
ALTER TABLE plate
ALTER COLUMN client_plate_db_id
    TYPE text
    USING client_plate_db_id::text;