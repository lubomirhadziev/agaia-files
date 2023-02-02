./gradlew build
./gradlew publish
./gradlew docker
echo "kokakola159753" | docker login -u lubomirhadziev --password-stdin
docker push lubomirhadziev/agaia-files:$(cat gradle.properties | grep 'VERSION=' | awk -F= '{print $2}')
doctl kubernetes cluster kubeconfig show 08a3d678-c6ee-42e2-b222-a2b8b2876d0d > deploy/kube-config.yaml
kubectl --kubeconfig=deploy/kube-config.yaml -n agaia apply -f deploy/app.yaml