`bandito`
=========

RESTful Multi Armed Bandit Service


Start the server:

	$ sbt run

Run the tests:

	$ sbt test

`RESTful API`:
==============

Valid characters for substitutions below(`<EXPERIMENT_ID>`, `<ARM_ID>`) are represented the `[a-zA-Z0-9_-]+` regex.

The only substitution for `<ALGORITHM>` is currently `EpsilonGreedy` and it will be used by default if the `algorithm` URL argument is not passed during a call to the CREATE endpoint.


`CREATE`
--------

When creating an experiment, pass the experiment id as the path to the service when sending HTTP `PUT` method. Optional URL arguments are:

	arm_ids: A comma separated list of arm identifiers, eg. "arm_ids=A,B,C" that match the regular expression [a-zA-Z0-9_-]+
	algorithm: Currently only 'EpsilonGreedy' is available. See the docs for each algorithm for the available parameters.
	

Examples using the cURL command:

	## Experiment id is required. If no 'algorithm' is provided, Epsilon Greedy is used. If no 'arm_ids' is provided, an arm is not created; one or more will be created when a call to UPDATE is received.
	$ curl -XPUT 'http://localhost:7070/<EXPERIMENT_ID>'

	## One or more arm ids are optional. If more than one are sent, each must be separated by a comma.
	$ curl -XPUT 'localhost:7070/<EXPERIMENT_ID>?arm_ids=<ARM_ID>'
	$ curl -XPUT 'localhost:7070/<EXPERIMENT_ID>?arm_ids=<ARM_ID>,<ARM_ID>,<ARM_ID>'

	## Passing an Algorithm name
	$ curl -XPUT 'localhost:7070/<EXPERIMENT_ID>?algorithm=<ALGORITHM>"'

	## Passing arguments along with the algorithm. See the Algorithm documentation for available arguments.
	$ curl -XPUT 'localhost:7070/<EXPERIMENT_ID>?arm_ids=<ARM_ID>&algorithm=<ALGORITHM>"&example_param_1=example&example_param_2=another_example'


If an arm id does not exist for an existing experiment, one will be created so `INCREMENT` and `UPDATE` can be called as soon as the service is running.

`UPDATE`
--------

When creating an experiment and one or more arms, the default counts for pulls and rewards are set to zero. The UPDATE command increments either value of `pulls` or `rewards`.

	## Update 'rewards' counts for an arm id.
	$ curl -XPUT 'localhost:7070/<EXPERIMENT_ID>/<ARM_ID>?reward=9.3'
	$ curl -XPUT 'localhost:7070/<EXPERIMENT_ID>/<ARM_ID>?reward=1100'

	## Update 'pulls' counts for an arm id.
	$ curl -XPUT 'localhost:7070/<EXPERIMENT_ID>/<ARM_ID>?pulls=11.1'
	$ curl -XPUT 'localhost:7070/<EXPERIMENT_ID>/<ARM_ID>?pulls=5500'


	## Update 'rewards' and 'pulls' counts for an arm id.
	$ curl -XPUT 'localhost:7070/<EXPERIMENT_ID>/<ARM_ID>?pulls=11.1&?reward=1100'
	$ curl -XPUT 'localhost:7070/<EXPERIMENT_ID>/<ARM_ID>?rewards=333&pulls=55'

`QUERY`
-------

The call to query retrieves information about an experiment or the state of the service.

	## Returns stats on all experiments and the state of the service
	$ curl -XGET 'localhost:7070'


	## Returns stats on an experiment
	$ curl -XGET 'localhost:7070/<EXPERIMENT_ID>'


`SELECT`
-------

Select an arm for an experiment id. Bandito handles exploration and exploitation of the arm ids. This call automatically increments the value of `pulls` for the selected arm.

	## Select an arm from all arms within the experiment.
	$ curl -XPOST 'localhost:7070/<EXPERIMENT_ID>'

	## Select an arm from the subset of the supplied arm ids.
	$ curl -XPOST 'localhost:7070/<EXPERIMENT_ID>?arm_ids=<ARM_ID>,<ARM_ID>,<ARM_ID>'


`DELETE`
--------

Delete an experiemnt or an experiment's arm id.

	## Delete an experiment
	$ curl -XDELETE 'localhost:7070/<EXPERIMENT_ID>'

    ## Delete an experiment's arm id.
	$ curl -XDELETE 'localhost:7070/<EXPERIMENT_ID>/<ARM_ID>'

