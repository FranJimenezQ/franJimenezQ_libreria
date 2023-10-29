def call(def abortOnQualityGateFailure = false, def abortPipeline = false) {
    sh 'echo “Ejecución de las pruebas de calidad de código”'
    
    timeout(time: 5, unit: 'MINUTES') {
        def qualityGateStatus = sh(script: 'curl -u sqb_d093caf346a02a6bf951405197b5514836b0c15c: http://localhost:9000/api/qualitygates/project_status?projectKey=DevOps_practica2', returnStdout: true).trim()
        
        if (qualityGateStatus.contains('"status":"ERROR"')) {
            def currentBranch = env.BRANCH_NAME
            
            if (abortOnQualityGateFailure) {
                error("Quality Gate falló.")
            } else if (currentBranch == 'main' || currentBranch.startsWith('hotfix')) {
                error("Quality Gate falló en una rama crítica.")
            }
        }
    }
    
    if (abortPipeline) {
        error("Pipeline abortado por el usuario.")
    }
}
