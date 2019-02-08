#!/usr/bin/groovy
parameters = [
	booleanParam(
		defaultValue: false,
		description: 'Reload Jenkinsfile',
		name: 'RELOAD'
	),
	choice(
		choices: 'java\npython',
		description: 'Available node',
		name: 'NODE'
	),
	string(
		defaultValue: '',
		description: 'Custom label for job build',
		name: 'LABEL'
	)
]

properties([
	parameters(parameters)
])

node {
	echo "Reload: ${params.RELOAD}, Node: ${params.NODE}, Build label: ${params.LABEL}"

	def reloadOnly = params.RELOAD || currentBuild.number == 1
	if (reloadOnly) {
		currentBuild.displayName = "Reload Jenkinsfile"
		return
	}

	if (params.LABEL) {
		currentBuild.displayName = "${params.LABEL}"
	}

    node("${params.NODE}") {
    	stage("Node information") {
    		def ip = sh(script: 'hostname -i', returnStdout:true).trim()
    		echo "IP: ${ip}"
    	}
    	stage("detect Programming language") {
    		def output = sh(script: '${NODE} -h', returnStdout:true).trim()
    		echo "${output}"
    	}
    }
}