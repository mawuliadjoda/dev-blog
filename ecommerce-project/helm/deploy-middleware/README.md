helm repo add traefik https://traefik.github.io/charts
helm repo update

# CRDs
helm install traefik-crds traefik/traefik-crds -n traefik --create-namespace

# Traefik (sans réinstaller les CRDs)
helm install traefik traefik/traefik -n traefik --skip-crds




kubectl apply -f middleware-oauth2-auth.yaml

lefort@GT13Pro:/mnt/d/dev/git/dev-blog/kubernetes/spring-boot-helm/deploy-forwardAuth$ kubectl apply -f middleware-oauth2-auth.yaml
middleware.traefik.io/oauth2-auth created





lefort@GT13Pro:/mnt/d/dev/git/dev-blog/ecommerce-project/helm/deploy-middleware$ kubectl apply -f middleware-oauth2-auth.yaml
middleware.traefik.io/forwardauth-no-redirect created
middleware.traefik.io/forwardauth-redirect created
middleware.traefik.io/errors-redirect created
middleware.traefik.io/redirect created
lefort@GT13Pro:/mnt/d/dev/git/dev-blog/ecommerce-project/helm/deploy-middleware$ kubectl api-resources | grep -i middleware
middlewares                                      traefik.io/v1alpha1               true         Middleware
middlewaretcps                                   traefik.io/v1alpha1               true         MiddlewareTCP
lefort@GT13Pro:/mnt/d/dev/git/dev-blog/ecommerce-project/helm/deploy-middleware$ kubectl get crd | grep -i traefik
ingressroutes.traefik.io          2026-01-05T15:08:46Z
ingressroutetcps.traefik.io       2026-01-05T15:08:46Z
ingressrouteudps.traefik.io       2026-01-05T15:08:46Z
middlewares.traefik.io            2026-01-05T15:08:46Z
middlewaretcps.traefik.io         2026-01-05T15:08:46Z
serverstransports.traefik.io      2026-01-05T15:08:46Z
serverstransporttcps.traefik.io   2026-01-05T15:08:46Z
tlsoptions.traefik.io             2026-01-05T15:08:46Z
tlsstores.traefik.io              2026-01-05T15:08:46Z
traefikservices.traefik.io        2026-01-05T15:08:46Z
lefort@GT13Pro:/mnt/d/dev/git/dev-blog/ecommerce-project/helm/deploy-middleware$ kubectl get middleware
NAME                      AGE
errors-redirect           66s
forwardauth-no-redirect   66s
forwardauth-redirect      66s
redirect                  66s