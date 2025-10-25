helm repo add traefik https://traefik.github.io/charts
helm repo update traefik

helm install traefik traefik/traefik




lefort@GT13Pro:/mnt/d/dev/git/dev-blog/ecommerce-project/helm/deploy-traefik$ helm repo add traefik https://traefik.github.io/charts
"traefik" already exists with the same configuration, skipping
lefort@GT13Pro:/mnt/d/dev/git/dev-blog/ecommerce-project/helm/deploy-traefik$ helm repo update traefik
Hang tight while we grab the latest from your chart repositories...
...Successfully got an update from the "traefik" chart repository
Update Complete. ⎈Happy Helming!⎈
lefort@GT13Pro:/mnt/d/dev/git/dev-blog/ecommerce-project/helm/deploy-traefik$ helm install traefik traefik/traefik
NAME: traefik
LAST DEPLOYED: Sat Oct 25 09:25:56 2025
NAMESPACE: default
STATUS: deployed
REVISION: 1
TEST SUITE: None
NOTES:
traefik with docker.io/traefik:v3.5.3 has been deployed successfully on default namespace !