
helm repo add oauth2-proxy https://oauth2-proxy.github.io/manifests
helm repo update

# cookieSecret
openssl rand -base64 32

openssl rand -base64 32
YmglTnMTSm2XqD1qvNnKzR2ffhp2pr20ijylhK4MzYA=

helm upgrade --install oauth2-proxy oauth2-proxy/oauth2-proxy --namespace default -f values.yaml