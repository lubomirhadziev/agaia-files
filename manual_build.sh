./gradlew build
./gradlew docker
echo "kokakola159753" | docker login -u lubomirhadziev --password-stdin
docker push lubomirhadziev/agaia-files:$(./gradlew properties -q | grep 'version:' | awk '{print $2}')
kubectl --kubeconfig=deploy/kube-config.yaml -n catering apply -f deploy/app.yaml