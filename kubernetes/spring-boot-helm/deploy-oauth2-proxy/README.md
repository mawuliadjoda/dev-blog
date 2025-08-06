
helm repo add oauth2-proxy https://oauth2-proxy.github.io/manifests
helm repo update

# cookieSecret
openssl rand -base64 32

helm upgrade --install oauth2-proxy oauth2-proxy/oauth2-proxy --namespace default -f oauth2-proxy-values.yaml