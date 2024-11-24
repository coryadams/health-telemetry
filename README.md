# HealthTelemetry Application

## Clickhouse

To get the Clickhouse binary to run locally on Linux, , macOS and FreeBSD:

curl https://clickhouse.com/ | sh

Start the Server

./clickhouse server

... and open a new terminal to connect to the server with clickhouse-client:

./clickhouse client



## Usage Notes

The files are included in the project and are referenced with a request of type FileRequest for testing locally
via PostMan or other client with the following message structure.

Example

{
"userName": "testUser@google.com",
"fileName": "17593651400_ACTIVITY.fit"
}

The Endpoints are as follows

{{base_url}}/fitfile/
{{base_url}}/tcxfile/

## Todo

1) Controller Should take a Multipart file as input along with FileRequest body.
2) Complete Repository for Clickhouse to store HealthEvent data.
3) Create a simple UI to take input for a user id and datetime range and display heart rate data in a graph.

