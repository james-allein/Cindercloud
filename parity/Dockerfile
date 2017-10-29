FROM parity/parity:v1.7.8

EXPOSE 8545
VOLUME /root/.local/share/io.parity.ethereum/chains

ADD start.sh /start.sh
RUN chmod +x /start.sh

ENTRYPOINT ["/start.sh"]