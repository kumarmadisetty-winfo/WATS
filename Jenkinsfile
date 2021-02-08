#!groovy

  node('master') {
	
	//def MAVEN_HOME= tool name: 'Maven3'
	// def scannerHome = tool 'SonarQubeScanner'
	REPO_URL='https://github.com/winfo-analytics/WATS.git'
    REPO='github.com/winfo-analytics/WATS.git'
    REPO_CRED='jenkins-cred'
    BRANCH_NAME='refs/heads/master'


    stage('Preparation') {
        println("Preparation...")
    }
	
    
    stage('Checkout Code') {
        echo "Checking out the code"
          checkout scm: [$class: 'GitSCM', userRemoteConfigs: [[url:REPO_URL, credentialsId:REPO_CRED]], branches: [[name:BRANCH_NAME]]],poll: false

    }
	
	stage ('Install') {
        echo "maven clean install..."
 	//	 withEnv(["PATH+MAVEN=$MAVEN_HOME/bin"]) {
			sh '/opt/apache-maven-3.6.3/bin/mvn  -U clean install'
	//	 }
    }
	
	// stage ('Component Vulnerability Scan') {
    //     echo "Component Vulnerability Testing..."
	// 	sh '/opt/dependency-check/bin/dependency-check.sh --project "ingestion" --scan . --out .'			
	// }
	
	
	// stage ('SonarQube Analysis') {
    //     echo "SonarQube Analysis..."
		
	// 	 withSonarQubeEnv('SONAR_QUBE_SERVER'){ 
    //         sh '/opt/sonar-scanner/bin/sonar-scanner -Dsonar.projectName=ingestion -Dsonar.projectKey=ingestion -Dsonar.sources=src/main/java -Dsonar.java.binaries=target/classes -Dsonar.scm.disabled=true -Dsonar.host.url=http://192.168.1.207:9000 -Dsonar.login=e9608d630b6d1bb4e2d472de00ceb9ec3854ea6b'
    //     }
    // }
	
	  stage ('SonarQube Analysis') {
        echo "SonarQube Analysis..."
		
		 withSonarQubeEnv('sonarqube'){ 
            sh '/opt/sonar-scanner/bin/sonar-scanner -Dsonar.projectName=wats_selenium -Dsonar.projectKey=ingestion -Dsonar.sources=src/main/java -Dsonar.java.binaries=target/classes -Dsonar.scm.disabled=true -Dsonar.host.url=http://192.168.1.207:9000 -Dsonar.login=9c38ce38703e38024d6d4d741c6145d3ee22fd70'
       }
     }
	 
	// stage ('Quality Gateway') {
    //     echo 'Need to pass Quality Gateway'  
		
	// 	timeout(time: 30, unit: 'SECONDS') {
    //           def qg = waitForQualityGate()
    //           if (qg.status != 'OK') {
    //               error "Pipeline aborted due to quality gate failure: ${qg.status}"
    //           }
    //        }	
    // }
	
	//  stage ('Unit Test') {
	// 	echo "Unit Test..."
		
	// 	  withEnv(["PATH+MAVEN=$MAVEN_HOME/bin"]) {                
	// 			sh 'mvn clean verify -B -U -e -fae -V -Dmaven.test.failure.ignore=true'                   				
	//     }		
    // }
	
     stage ('Package') { 
		echo 'Package...'
		
		// withEnv(["PATH+MAVEN=$MAVEN_HOME/bin"]) {
			sh '/opt/apache-maven-3.6.3/bin/mvn dependency:resolve -U -B -DskipTests clean package'
		 //}
    }
	  
	  stage('Upload War To Nexus'){
           
                    def mavenPom = readMavenPom file: 'pom.xml'
                    def nexusRepoName = mavenPom.version.endsWith("SNAPSHOT") ? "wats_snapshot" : "wats_release"
                    nexusArtifactUploader artifacts: [
                        [
                            artifactId: 'WinfoAutomation', 
                            classifier: '', 
                            file: "target/${mavenPom.name}.war", 
                            type: 'war'
                        ]
                    ], 
                    credentialsId: 'nexus3', 
                    groupId: 'com.automationtesting', 
                    nexusUrl: 'winfosys103.winfosolutions.com:8091', 
                    nexusVersion: 'nexus3', 
                    protocol: 'http', 
                    repository: nexusRepoName, 
                    version: "${mavenPom.version}"
            
        }
	  
	 //Ansible command
	 ansiColor('xterm') {
    ansiblePlaybook( 
        playbook: '/var/lib/jenkins/workspace/testGit/copyfile.yml',
        inventory: '/etc/ansible/hosts',
	colorized: true) 
   }
	
  // Selenium validation 
     stage('Validation') {
     sh """
       echo " Updating validations script"
       cp -p /var/lib/jenkins/workspace/testGit/Validations.sh_org /var/lib/jenkins/workspace/testGit/Validations.sh
       sed -i -e  "s|UBVMNAME|watsselt01|g"  /var/lib/jenkins/workspace/testGit/Validations.sh
       echo " sleep for 1min"
       sleep 1m
       echo "Validating of wats.war on ubuntu server"
       . /var/lib/jenkins/workspace/testGit/Validations.sh  > /tmp/Validations.log
       """
      }
                
	// stage ('Generate Latesttag') {
	// 	echo "Generate Next Tag"
    //          TAG = sh(returnStdout: true, script: "git describe --tags `git rev-list --tags --max-count=1`").trim()
    //          echo TAG
    //          env.MINEINGOLDTAG=TAG
    //          sh "chmod 777 version.sh"
    //          TAG_NAME = sh(returnStdout: true, script: "bash version.sh").trim()
    //          echo TAG_NAME
    // }

	
	// stage ('Docker Image') { 
	// 	echo 'Create Docker Image...'
	// 		sh "docker build -t ${NEXUS_DOCKER_REPOSITORY}/ingestion:${TAG_NAME} --no-cache ."
    // }
	
	// stage ('Push Docker Image') { 
	// 	echo 'Push Docker Image...'
	// 		sh "docker push ${NEXUS_DOCKER_REPOSITORY}/ingestion:${TAG_NAME}"
    // }

	// stage ('Update Yaml Files') { 
	// 	echo 'Updating Kubernetes scripts with latest tag'
    //          sh "sed -i -e s,ingestion-1.0,${TAG_NAME},g ingestion-deployment.yaml"
    // }

	// 	stage ('Deploy to Kubernetes Cluster') { 
	// 	echo 'Deploying to Kubernetes Cluster...'
		
	// 	sh "kubectl apply -f  ingestion-clusterip.yaml"	
	// 	sh "kubectl apply -f  ingestion-deployment.yaml" 
    
	// }
	
	// stage ('Performance Testing') {
    //     echo "Rest API Testing using JMeter..."
    // }
	
	// stage ('Functional Testing') {
    //     echo "Functional testing..."
	 //     echo "Functional testing..."
    // }
      } 
