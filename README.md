# Data Canvas Service

A service that manages Pennsieve Data Canvases. 

## Testing

Run the unit tests with

    sbt test

There is an integration test suite that runs against the DataCite sandbox API using the `non-prod` DataCite credentials. You must assume a role in the `non-prod` account to run these tests:

    assume-role non-prod admin
    sbt integration:test

## Releasing

Every merge to `main` pushes a new version of `doi-service-client` to Nexus. The published JAR is versioned with the same Jenkins image tag as the service Docker container.
