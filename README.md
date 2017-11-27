# Cindercloud [![Build Status](https://travis-ci.org/Cindercloud/Cindercloud.svg?branch=master)](https://travis-ci.org/Cindercloud/Cindercloud)
Ethereum Chain Explorer

![intro](https://cdn.rawgit.com/Cindercloud/Cindercloud/master/web/src/main/resources/static/assets/images/introduction.svg)

## Modules

Following modules are available and can run independently

### Web Module

The module you can see at https://cinder.cloud. This is the main blockchain explorer, as well as wallet management

###  Importer Module

Provides functionality to transform the blockchain into our own model.

- Live import
- Historic Import
- Transaction Import from Block-Queue (offloaded)

### Whitehat Module

Provides functionality to protect users from mistakes before blackhats do it. 

- Known Private Keys get monitored
- Phishing attempts get Logged
- ...
