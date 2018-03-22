FROM debian
WORKDIR /usr/src/app
RUN apt-get -qq update
RUN apt-get -y install curl gnupg apt-transport-https git
RUN curl -sS https://dl.yarnpkg.com/debian/pubkey.gpg | apt-key add -
RUN curl -sL https://deb.nodesource.com/setup_8.x | bash -
RUN echo "deb https://dl.yarnpkg.com/debian/ stable main" | tee /etc/apt/sources.list.d/yarn.list
RUN apt-get -qq update
RUN apt-get -y install yarn
ENTRYPOINT ["sh", "entry.sh"]
CMD ["yarn", "start"]
