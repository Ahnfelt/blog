#################
# Initial setup
#################

# Blog directory
# mkdir www
# mkdir blog

# Get caddy
# echo "deb [trusted=yes] https://apt.fury.io/caddy/ /" \
#     | sudo tee -a /etc/apt/sources.list.d/caddy-fury.list
# sudo apt update
# sudo apt install caddy

# Get stack
# wget -qO- https://get.haskellstack.org/ | sh

# Get screen
# apt install screen

# Clone the repository
# git clone https://github.com/Ahnfelt/blog.git

# Make a directory for caddy logs
# mkdir /var/log/caddy && chown caddy:caddy /var/log/caddy && chmod o-rwx /var/log/caddy

# Make a script that runs in a loop, greps for the hook, if it changed:
# (cd ../../blog && git pull && bash update.sh)


#################
# Update script
#################

