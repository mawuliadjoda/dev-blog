

# 
    sudo apt update
    sudo apt install -y curl zip unzip
    curl -s "https://get.sdkman.io" | bash
    source "$HOME/.sdkman/bin/sdkman-init.sh" => reload shell
    

    sdk version
    sdk list java
    sdk install java  25.0.1-tem
    java -version
