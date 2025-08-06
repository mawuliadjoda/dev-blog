helm upgrade --install traefik traefik/traefik --namespace kube-system --create-namespace -f traefik-values.yaml

helm repo add traefik https://traefik.github.io/charts
helm repo update traefik

helm install traefik traefik/traefik