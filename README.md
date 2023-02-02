# agaia-files


### Create kubernetes namespace (once)

```
kubectl --kubeconfig=deploy/kube-config.yaml apply -f deploy/namespace.yaml
```


### Deploy project to kubernetes

```
kubectl --kubeconfig=deploy/kube-config.yaml -n agaia apply -f deploy/app.yaml
```

### SUB Module

```
build.gradle file must include

bootJar {
    enabled = false
}

jar {
    enabled = true
}
```