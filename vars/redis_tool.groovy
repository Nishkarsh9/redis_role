def call() {
    // Load configuration from config.yml file
    def config = readYaml file: 'Assignment-06@libs/8e000fc4ebcf6479b76459255907e8ded33a5f9f91b08021e1ebf509c0256fef/resources/config.yml'

    // Step 1: Clone the specific Git repository and checkout the desired branch
    sh 'git clone https://github.com/Nishkarsh9/image.git'
    sh 'cd image'

    // Step 2: User Approval (if needed)
    if (config.KEEP_APPROVAL_STAGE.toBoolean()) {
        input message: 'Approve to continue with Redis deployment?', ok: 'Deploy'
    }

    // Step 3: Execute Ansible Playbook
    try {
        ansiblePlaybook(
            playbook: 'playbook.yml',
            extraVars: [
                CODE_BASE_PATH: config.CODE_BASE_PATH,
                ENVIRONMENT: config.ENVIRONMENT
            ],
            become: true,  // If you need to run with sudo privileges
            becomeUser: 'root'  // Optionally specify user (if needed)
        )
    } catch (Exception e) {
        currentBuild.result = 'FAILURE'
        throw e
    }

    
}
