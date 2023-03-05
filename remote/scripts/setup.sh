#!/usr/bin/env bash
# Install snapd
sudo apt install snapd

# Install certbot
# https://certbot.eff.org/instructions?ws=other&os=ubuntufocal
sudo snap install core; sudo snap refresh core
sudo snap install --classic certbot
sudo ln -s /snap/bin/certbot /usr/bin/certbot

# Run certbot
sudo certbot certonly --standalone -d public.nipsr.com