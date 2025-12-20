
helm repo add oauth2-proxy https://oauth2-proxy.github.io/manifests
helm repo update

# cookieSecret
openssl rand -base64 32

openssl rand -base64 32
YmglTnMTSm2XqD1qvNnKzR2ffhp2pr20ijylhK4MzYA=

helm upgrade --install oauth2-proxy oauth2-proxy/oauth2-proxy --namespace default -f values.yaml



# Endpoints les plus utiles

http://adjodadev.com/oauth2/start
http://adjodadev.com/oauth2/auth
http://adjodadev.com/oauth2/userinfo
    {
    "user": "aa8c6451-2d00-407d-9a63-ae5d3d731b60",
    "email": "koffimawuli.adjoda@gmail.com",
    "groups": [
    "role:CHEF_AGENCE",
    "role:offline_access",
    "role:default-roles-adjoda-dev-realm",
    "role:uma_authorization",
    "role:account:manage-account",
    "role:account:manage-account-links",
    "role:account:view-profile"
    ],
    "preferredUsername": "mawuli"
    }
http://adjodadev.com/oauth2/healthz
http://adjodadev.com/oauth2/sign_out

1) /oauth2/start  => http://adjodadev.com/oauth2/start

Démarre le flow de login (redirige vers l’IdP).

Souvent appelé automatiquement quand une route protégée renvoie 401/302.

Tu peux aussi l’utiliser manuellement pour “forcer” une connexion.


2) /oauth2/sign_out

Déconnecte la session oauth2-proxy (supprime le cookie).

Attention : ça ne déconnecte pas forcément l’IdP (SSO), selon config.

5) /oauth2/userinfo

Renvoie les infos utilisateur vues par oauth2-proxy (souvent JSON).

Très pratique pour tester rapidement “qui suis-je” côté proxy.

6) /oauth2/auth (celui que tu as trouvé)

Endpoint technique “auth_request/forwardauth”.

Répond 200/401 et peut renvoyer des headers d’identité (et parfois tokens si configuré).