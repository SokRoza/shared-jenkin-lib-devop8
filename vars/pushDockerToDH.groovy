def call(String imageName){
    withCredentials([usernamePassword(credentialsId: 'DOCKERHUB', passwordVariable: 'PASSWORD', usernameVariable: 'USERNAME')]) {    
        sh """
            echo '$PASSWORD' |  docker login -u $USERNAME --password-stdin   
        docker push ${imageName}
        """
                    
    }
}