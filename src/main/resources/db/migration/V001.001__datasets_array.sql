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

-- Updates trial.additional_info to have a datasets array instead of just observationDatasetId.
-- Leaves observationDatasetId in place out of an abundance of caution.
-- Only updates rows that don't already have datasets key (just in case the code was updated prematurely).
DO
$$
BEGIN
    UPDATE
        trial
    SET
        additional_info = additional_info
            || JSONB_BUILD_OBJECT(
                'datasets',
                JSONB_BUILD_ARRAY(
                    JSONB_BUILD_OBJECT(
                        'id', additional_info->'observationDatasetId',
                        'name', additional_info->'defaultObservationLevel',
                        'level', '0'
                    )
                )
            )
    WHERE
        additional_info->'datasets' IS NULL;
END
$$;
