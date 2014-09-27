#!/bin/sh

################# CREATION
# Create a new experiment
curl -XPUT 'localhost:7070/myNewExperiment'

# Create a new experiment with 3 conditions
curl -XPUT 'localhost:7070/myNewExperimentWithArms?arms=arm1,anotherArm,aThirdArm'

# Get some basic info back about model stats
curl -XGET 'localhost:7070' | pjson

################# QUERYING
# Select a condition for me
curl -XGET 'localhost:7070/myNewExperimentWithArms'
curl -XGET 'localhost:7070/myNewExperimentWithArms'
curl -XGET 'localhost:7070/myNewExperimentWithArms'
curl -XGET 'localhost:7070/myNewExperimentWithArms'

# Banditod creates a new experiment with a DEFAULT arm if you forgot to either
# create the experiment:
curl -XGET 'localhost:7070/forgotToMakeAnExperiment'

# ...or if you forget to add any conditions
curl -XGET 'localhost:7070/myNewExperiment'

################# DELETION
# Kill a whole experiment
curl -XDELETE 'localhost:7070/myNewExperiment'

# Remove a single condition from an experiment
curl -XDELETE 'localhost:7070/myNewExperimentWithArms/arm1'

# Helpful error messages if you've already deleted things!
curl -XDELETE 'localhost:7070/myNewExperiment'
curl -XDELETE 'localhost:7070/myNewExperimentWithArms/arm1'

################# FEEDBACK
# Update a condition with a specific reward
curl -XPOST 'localhost:7070/myNewExperimentWithArms/anotherArm?reward=9.3'
# ...or without one (model's provide defaults)!
curl -XPOST 'localhost:7070/myNewExperimentWithArms/anotherArm'

# Experiments and arms are created on-the-fly!
curl -XPOST 'localhost:7070/myNewExperimentWithArms/heresANewArm'
curl -XPOST 'localhost:7070/aNewExperiment/andANewArm?reward=1.0'
