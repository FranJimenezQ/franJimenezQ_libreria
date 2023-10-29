def call(def abortOnQualityGateFailure = false, def abortPipeline = false) {
    sh 'echo “Ejecución de las pruebas de calidad de código”'
    
    timeout(time: 5, unit: 'MINUTES') {
        def qualityGateStatus = sh(script: 'curl -u [![Quality Gate Status]
        (http://localhost:9000/api/project_badges/measure?project=DevOps_practica2&metric=alert_status&token=sqb_d093caf346a02a6bf951405197b5514836b0c15c)]
        (http://localhost:9000/dashboard?id=DevOps_practica2): http://localhost:9000/api/qualitygates/project_status?projectKey=DevOps_practica2', 
        returnStdout: true).trim()
        
        if (qualityGateStatus.contains('"status":"ERROR"')) {
            def currentBranch = env.BRANCH_NAME
            
            if (abortOnQualityGateFailure) {
                error("Quality Gate falló.")
            } else if (currentBranch == 'master' || currentBranch.startsWith('hotfix')) {
                error("Quality Gate falló en una rama crítica.")
            }
        }
    }
    
    if (abortPipeline) {
        error("Pipeline abortado por el usuario.")
    }
}
