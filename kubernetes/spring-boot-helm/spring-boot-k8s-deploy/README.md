kubectl apply -f k8s-config.yaml
kubectl apply -f ingress.yaml
kubectl get all

# Remarque importante : Absence de minikube tunnel
Sans minikube tunnel, les services ClusterIP ou exposés via Ingress ne sont pas accessibles depuis Windows.

minikube tunnel



lefort@GT13Pro:~$ minikube ip
192.168.49.2

# Dans /etc/hosts  ==> Utiliser minikube tunnel et rediriger via 127.0.0.1, ping 192.168.49.2 (Essayer de pinguer 192.168.49.2 depuis Windows not working)
#192.168.49.2 adjodadev.com  

127.0.0.1 adjodadev.com