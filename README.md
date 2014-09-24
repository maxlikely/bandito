bandito
=======

Multi Armed Bandit solutions


Start the server:

    $ sbt run

The service methods:

    DELETE: remove an experiment or an arm related to an experiment.
    INCREMENT: increment a field on an arm.
    QUERY: query the values for an experiment or arms.

If an arm id does not exist one will be created so `INCREMENT` and `UPDATE` can be called as soon as the service is running.

_Each service method accepts one or more required arguments:_

DELETE:<br />

- experiment id: experiment identifier<br />
- arm id: arm identifier<br />
_or_<br />
- experiment id: experiment identifier<br />
- Returns HTTP 200 on success

INCREMENT:<br />

- experiment id: experiment identifier<br />
- arm id: arm identifier<br />
- rewards: count to increment on the arm's reward behavior<br />
- offers: count of total arm pulls to increment<br />
- Returns HTTP 200 on success

QUERY:<br />

- experiment id: experiment identifier<br />
- arm ids: a list of one or more arm identifiers<br />
_or_<br />
- experiment id: experiment identifier<br />

- Returns HTTP 200 and a JSON payload of summary stats on the item(s) queried

Currently using [http://activate-framework.org/](http://activate-framework.org/) for data persistence.

Example curls:

    # Increment the 'rewards' value by 1 for experiment id 1 and arm id A
    $ curl "localhost:8080/increment?expId=1&armId=A&field=rewards&value=1"

    # Update the 'rewards' to 100 and the 'offers' to 300 for experiment id 1 and arm id A
    $ curl "localhost:8080/update?expId=1&armId=A&rewards=100&offers=300"
