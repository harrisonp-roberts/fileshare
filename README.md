# (Unnamed) File Sharing Application

## Docker Compose Config:
- Environment variables
  - HOST: The URL the application is hosted on. Used when providing download links
  - REDIS_SERVER: The hostname of the redis instance that is attached to the bridge network
  - REDIS_PORT (Opt): Used if redis is running on non-default port
- Volumes
  - /files:/files
    - Directory mapping for file storage
  - /cache:/tmp/cache
    - Directory mapping for tempory file cache

## Running Development:
- Requires JDK version 21+
- Running development should be as easy as clicking run in IntelliJ.
  1. Create directories to use for file storage and cache in development environment
  2. Edit the run configuration, and add a task to set environment variables FILE_STORE_DIR and FILE_CACHE_DIR
  3. Ensure redis is running on default port (if running on other IP + Port, configure those as environment variables too)
  4. Run the application

## Running Production:
- Requires JDK version 21+
- Run the script in dev-ops to build a docker image containing the project jar
- In the dev-ops/build directory, configure the docker-compose file
- Run the docker compose file. This will spin up the fileshare application and a redis instance. They will be connected on a bridge network and the fileshare application will be exposed on port 8888 by default