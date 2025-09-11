- Verified in Postman:
    * Happy path (Budapest) -> 200, local times with +02:00
    * Default tz (no tz passed) -> 200, UTC
    * Missing city -> 400 MISSING_PARAMETER
    * City not found -> 404 CITY_NOT_FOUND
    * Invalid tz (spaces/typos) -> 400 INVALID_TIMEZONE
    * Invalid date format -> 400 TYPE_MISMATCH
    * Broken upstream URL -> 502 UPSTREAM_ERROR