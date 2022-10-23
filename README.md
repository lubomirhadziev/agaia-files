# agaia-files


### Create kubernetes namespace (once)

```
kubectl --kubeconfig=deploy/kube-config.yaml apply -f deploy/namespace.yaml
```


### Deploy project to kubernetes

```
kubectl --kubeconfig=deploy/kube-config.yaml -n catering apply -f deploy/app.yaml
```

