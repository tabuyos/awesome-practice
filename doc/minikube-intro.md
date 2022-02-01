# 参考

- [minikube start | minikube](https://minikube.sigs.k8s.io/docs/start/)



## Install

```shell
# Linux
curl -LO https://storage.googleapis.com/minikube/releases/latest/minikube-linux-amd64
sudo install minikube-linux-amd64 /usr/local/bin/minikube

# MacOS
curl -LO https://storage.googleapis.com/minikube/releases/latest/minikube-darwin-amd64
sudo install minikube-darwin-amd64 /usr/local/bin/minikube

# Windows
# use package manager
winget install minikube
```



## Start cluster

```shell
minikube start
```



## Interact with your cluster

```shell
# use native kubectl tool
kubectl get po -A

# use minikube to download kubectl tool
minikube kubectl -- get po -A

# make alias
alias kubectl="minikube kubectl --"
```



## Deploy applications

```shell
# create a sample application
kubectl create deployment hello-minikube --image=k8s.gcr.io/echoserver:1.4
kubectl expose deployment hello-minikube --type=NodePort --port=8080

# use kubectl to deployment
kubectl get services hello-minikube

# or use minikube to deployment
minikube service hello-minikube

# forward port
kubectl port-forward service/hello-minikube 7080:8080
```



### LoadBalancer deployments

```shell
# create a sample application
kubectl create deployment balanced --image=k8s.gcr.io/echoserver:1.4  
kubectl expose deployment balanced --type=LoadBalancer --port=8080

# access a LoadBalancer deployment
minikube tunnel

# find routable ip(in EXTERNAL-IP column)
kubectl get services balanced
```



##  Manage your cluster

```shell
# pause Kubernetes without impacting deployed applications
minikube pause

# unpause a instance
minikube unpause

# halt a cluster
minikube stop

# increase the default memory limit (requires a restart)
minikube config set memory 16384

# browse the catalog of easily installed Kubernetes services
minikube addons list

# create a second cluster running an older Kubernetes release
minikube start -p aged --kubernetes-version=v1.16.1

# delete all of the minikube clusters
minikube delete --all
```

