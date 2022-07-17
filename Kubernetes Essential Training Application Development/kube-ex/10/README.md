# WARNING
NetworkPolicy is enforced by your CNI plugin, and they don't all do it.
Minikube seems to use bridge mode or maybe even kubenet, and that doesn't enforce network policy.
Likewise defaut GKE also doesn't, because they use bridge mode.
Easiest way to demo this is GKE with Calico.
