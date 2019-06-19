pipeline {
    agent any
    
    stages {
        stage ('clone'){
            steps {
                checkout([$class: 'GitSCM', 
                branches: [[name: '*/develop_backend']], 
                doGenerateSubmoduleConfigurations: false, 
                extensions: [], 
                submoduleCfg: [], 
                userRemoteConfigs: [[credentialsId: 'git credentials', 
                url: 'https://github.com/daluas/RoomPlanner.git']]])
            }
        }
        stage ('build project'){
            steps {
                dir ('backend/RoomPlanner/room-planner') {
                    sh 'mvn clean install -DskipTests'   
                }
            }
        }
        stage ('undeploy'){
            steps {
                sh 'sudo fuser -k 8081/tcp'
            }
        }
        stage ('deploy'){
            steps {
                dir ('backend/RoomPlanner/room-planner/target') {
                    sh 'sudo java -Dspring.datasource.url=jdbc:postgresql://178.22.68.114:5432/room_planner -jar room-planner-0.0.1-SNAPSHOT.jar &'   
                }
            }
        }
    }
}