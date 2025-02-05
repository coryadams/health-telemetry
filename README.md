# HealthTelemetry Application

## Clickhouse

To get the Clickhouse binary to run locally on Linux, macOS and FreeBSD:

curl https://clickhouse.com/ | sh

Start the Server

./clickhouse server

... and open a new terminal to connect to the server with clickhouse-client:

./clickhouse client


## Usage Notes

The files are included in the project and are referenced with a request of type FileRequest for testing locally
via PostMan or other client with the following message structure.


The Request Endpoints are as follows

    {{base_url}}/fitfile/
    {{base_url}}/tcxfile/

Example JSON Request Body

    {
    "userName": "testUser@google.com",
    "fileName": "17593651400_ACTIVITY.fit"
    }

## Obersvability with OTEL

This application will use Open Telemetry for Traces, Metrics and Logging with the ability to 
store OTLP data into Clickhouse

Installation of the OTEL Collector is here : https://opentelemetry.io/docs/collector/installation/

For Linux 

If you modify the Collector configuration file or /etc/otelcol/otelcol.conf, restart the otelcol service to apply the changes by running:

sudo systemctl restart otelcol

To check the output from the otelcol service, run:

sudo journalctl -fu otelcol

## Todo
- Add other activity data in the FIT processor to align with TCX from HealthEventContainer
- Integrate Spring Security https://bootify.io/spring-security/form-login-with-spring-boot-thymeleaf.html
- Have the file upload land on an endpoint that routes based on file extension {TCX, FIT}

HD : For evaluating performance:
- Consider getting location data for alt, lat, long, temp - Do we need GPX files for this?
