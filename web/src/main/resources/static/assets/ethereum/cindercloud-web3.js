var CindercloudWeb3 = (function () {
	var _web3;
	var _globalWeb3;
	var _clientWeb3Enabled;

	var initiateWeb3 = function () {
		if (typeof window.web3 !== 'undefined') {
			_clientWeb3Enabled = true;
			// Use Mist/MetaMask's provider
			console.log("already had a web3");
			_web3 = new Web3(window.web3.currentProvider);
			$('.clientSideWeb3Enabled').show();
			$('.clientSideWeb3Disabled').hide();
		} else {
			_clientWeb3Enabled = false;
			$('.clientSideWeb3Enabled').hide();
			$('.clientSideWeb3Disabled').show();
			console.log("no web3");
		}
		_globalWeb3 = new Web3(new Web3.providers.HttpProvider('https://mainnet.fundrequest.io'));
	};

	initiateWeb3();

	return {
		getWeb3: function () {
			if (_clientWeb3Enabled) {
				return _web3
			} else {
				return _globalWeb3
			}
		},
		getGlobalWeb3: function () {
			return _globalWeb3;
		}
	}
})();
