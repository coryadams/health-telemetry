# HealthTelemetry Application

## Clickhouse

To get the Clickhouse binary to run locally on Linux, , macOS and FreeBSD:

curl https://clickhouse.com/ | sh

Start the Server

./clickhouse server

... and open a new terminal to connect to the server with clickhouse-client:

./clickhouse client

## Test locally Postman

This will rely on a file in the Resources directory which is a Garmin TCX file: 

http://localhost:8080/tcxfile/activity_11_21_2024_gym.tcx