var Kyber = (function () {

	var address = $('#currentAddress').val();

	var kyberMainnet = '0x964F35fAe36d75B1e72770e244F6595B68508CF5';

	var kyberabi = {
		expected_rate: [{"constant":true,"inputs":[{"name":"src","type":"address"},{"name":"dest","type":"address"},{"name":"srcQty","type":"uint256"}],"name":"getExpectedRate","outputs":[{"name":"expectedRate","type":"uint256"},{"name":"slippageRate","type":"uint256"}],"payable":false,"stateMutability":"view","type":"function"}],
	};

	var erc20ABI = [
		{
			"constant": true,
			"inputs": [],
			"name": "name",
			"outputs": [
				{
					"name": "",
					"type": "string"
				}
			],
			"payable": false,
			"type": "function"
		},
		{
			"constant": false,
			"inputs": [
				{
					"name": "_spender",
					"type": "address"
				},
				{
					"name": "_value",
					"type": "uint256"
				}
			],
			"name": "approve",
			"outputs": [
				{
					"name": "success",
					"type": "bool"
				}
			],
			"payable": false,
			"type": "function"
		},
		{
			"constant": true,
			"inputs": [],
			"name": "totalSupply",
			"outputs": [
				{
					"name": "",
					"type": "uint256"
				}
			],
			"payable": false,
			"type": "function"
		},
		{
			"constant": false,
			"inputs": [
				{
					"name": "_from",
					"type": "address"
				},
				{
					"name": "_to",
					"type": "address"
				},
				{
					"name": "_value",
					"type": "uint256"
				}
			],
			"name": "transferFrom",
			"outputs": [
				{
					"name": "success",
					"type": "bool"
				}
			],
			"payable": false,
			"type": "function"
		},
		{
			"constant": true,
			"inputs": [],
			"name": "decimals",
			"outputs": [
				{
					"name": "",
					"type": "uint8"
				}
			],
			"payable": false,
			"type": "function"
		},
		{
			"constant": true,
			"inputs": [],
			"name": "version",
			"outputs": [
				{
					"name": "",
					"type": "string"
				}
			],
			"payable": false,
			"type": "function"
		},
		{
			"constant": true,
			"inputs": [
				{
					"name": "_owner",
					"type": "address"
				}
			],
			"name": "balanceOf",
			"outputs": [
				{
					"name": "balance",
					"type": "uint256"
				}
			],
			"payable": false,
			"type": "function"
		},
		{
			"constant": true,
			"inputs": [],
			"name": "symbol",
			"outputs": [
				{
					"name": "",
					"type": "string"
				}
			],
			"payable": false,
			"type": "function"
		},
		{
			"constant": false,
			"inputs": [
				{
					"name": "_to",
					"type": "address"
				},
				{
					"name": "_value",
					"type": "uint256"
				}
			],
			"name": "transfer",
			"outputs": [
				{
					"name": "success",
					"type": "bool"
				}
			],
			"payable": false,
			"type": "function"
		},
		{
			"constant": false,
			"inputs": [
				{
					"name": "_spender",
					"type": "address"
				},
				{
					"name": "_value",
					"type": "uint256"
				},
				{
					"name": "_extraData",
					"type": "bytes"
				}
			],
			"name": "approveAndCall",
			"outputs": [
				{
					"name": "success",
					"type": "bool"
				}
			],
			"payable": false,
			"type": "function"
		},
		{
			"constant": true,
			"inputs": [
				{
					"name": "_owner",
					"type": "address"
				},
				{
					"name": "_spender",
					"type": "address"
				}
			],
			"name": "allowance",
			"outputs": [
				{
					"name": "remaining",
					"type": "uint256"
				}
			],
			"payable": false,
			"type": "function"
		},
		{
			"inputs": [
				{
					"name": "_initialAmount",
					"type": "uint256"
				},
				{
					"name": "_tokenName",
					"type": "string"
				},
				{
					"name": "_decimalUnits",
					"type": "uint8"
				},
				{
					"name": "_tokenSymbol",
					"type": "string"
				}
			],
			"type": "constructor"
		},
		{
			"payable": false,
			"type": "fallback"
		},
		{
			"anonymous": false,
			"inputs": [
				{
					"indexed": true,
					"name": "_from",
					"type": "address"
				},
				{
					"indexed": true,
					"name": "_to",
					"type": "address"
				},
				{
					"indexed": false,
					"name": "_value",
					"type": "uint256"
				}
			],
			"name": "Transfer",
			"type": "event"
		},
		{
			"anonymous": false,
			"inputs": [
				{
					"indexed": true,
					"name": "_owner",
					"type": "address"
				},
				{
					"indexed": true,
					"name": "_spender",
					"type": "address"
				},
				{
					"indexed": false,
					"name": "_value",
					"type": "uint256"
				}
			],
			"name": "Approval",
			"type": "event"
		}
	];

	var kyberData = {
		sources: [],
		targets: [],
		source: null,
		target: null,
		targetAmount: 0,
		sourceAmount: 0,
		expectedRate: 0,
		slippageRate: 0,
		balance: 0,
		tokens: {
			ETH: {
				name: "Ethereum",
				symbol: "ETH",
				icon: "eth.svg",
				address: "0xeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeee",
				image: "ethereum_1",
				decimal: 18,
				usd_id: "ethereum"
			},
			KNC: {
				name: "KyberNetwork",
				symbol: "KNC",
				icon: "knc.svg",
				address: "0xdd974d5c2e2928dea5f71b9825b8b646686bd200",
				image: "0xdd974d5c2e2928dea5f71b9825b8b646686bd200",
				decimal: 18,
				usd_id: "kyber-network"
			},
			OMG: {
				name: "OmiseGO",
				symbol: "OMG",
				icon: "omg.svg",
				address: "0xd26114cd6ee289accf82350c8d8487fedb8a0c07",
				image: "0xd26114cd6ee289accf82350c8d8487fedb8a0c07",
				decimal: 18,
				usd_id: "omisego"
			},
			EOS: {
				name: "Eos",
				symbol: "EOS",
				icon: "eos.svg",
				address: "0x86fa049857e0209aa7d9e616f7eb3b3b78ecfdb0",
				image: "0x86fa049857e0209aa7d9e616f7eb3b3b78ecfdb0",
				decimal: 18,
				usd_id: "eos"
			},
			SNT: {
				name: "Status",
				address: "0x744d70fdbe2ba4cf95131626614a1763df805b9e",
				image: "0x744d70fdbe2ba4cf95131626614a1763df805b9e",
				symbol: "SNT",
				icon: "snt.svg",
				decimal: 18,
				usd_id: "status"
			},
			ELF: {
				name: "Aelf",
				address: "0xbf2179859fc6d5bee9bf9158632dc51678a4100e",
				image: "0xbf2179859fc6d5bee9bf9158632dc51678a4100e",
				symbol: "ELF",
				icon: "aelf.svg",
				decimal: 18,
				usd_id: "aelf"
			},
			POWR: {
				name: "Power Ledger",
				address: "0x595832f8fc6bf59c85c527fec3740a1b7a361269",
				image: "0x595832f8fc6bf59c85c527fec3740a1b7a361269",
				symbol: "POWR",
				icon: "pwr.svg",
				decimal: 6,
				usd_id: "power-ledger"
			},
			MANA: {
				name: "Mana",
				address: "0x0f5d2fb29fb7d3cfee444a200298f468908cc942",
				image: "0x0f5d2fb29fb7d3cfee444a200298f468908cc942",
				symbol: "MANA",
				icon: "mana.svg",
				decimal: 18,
				usd_id: "decentraland"
			},
			BAT: {
				name: "Basic Attention Token",
				address: "0x0d8775f648430679a709e98d2b0cb6250d2887ef",
				image: "0x0d8775f648430679a709e98d2b0cb6250d2887ef",
				symbol: "BAT",
				icon: "bat.svg",
				decimal: 18,
				usd_id: "basic-attention-token"
			},
			REQ: {
				name: "Request",
				address: "0x8f8221afbb33998d8584a2b05749ba73c37a938a",
				image: "0x8f8221afbb33998d8584a2b05749ba73c37a938a",
				symbol: "REQ",
				icon: "req.svg",
				decimal: 18,
				usd_id: "request-network"
			},
			GTO: {
				name: "Gifto",
				address: "0xc5bbae50781be1669306b9e001eff57a2957b09d",
				image: "0xc5bbae50781be1669306b9e001eff57a2957b09d",
				symbol: "GTO",
				icon: "gifto.svg",
				decimal: 5,
				usd_id: "gifto"
			},
			RDN: {
				name: "Raiden",
				address: "0x255aa6df07540cb5d3d297f0d0d4d84cb52bc8e6",
				image: "0x255aa6df07540cb5d3d297f0d0d4d84cb52bc8e6",
				symbol: "RDN",
				icon: "rdn.svg",
				decimal: 18,
				usd_id: "raiden-network-token"
			},
			APPC: {
				name: "AppCoins",
				address: "0x1a7a8bd9106f2b8d977e08582dc7d24c723ab0db",
				image: "0x1a7a8bd9106f2b8d977e08582dc7d24c723ab0db",
				symbol: "APPC",
				icon: "appc.svg",
				decimal: 18,
				usd_id: "appcoins"
			},
			ENG: {
				name: "Enigma",
				address: "0xf0ee6b27b759c9893ce4f094b49ad28fd15a23e4",
				image: "0xf0ee6b27b759c9893ce4f094b49ad28fd15a23e4",
				symbol: "ENG",
				icon: "eng.svg",
				decimal: 8,
				usd_id: "enigma-project"
			},
			SALT: {
				name: "Salt",
				address: "0x4156d3342d5c385a87d264f90653733592000581",
				image: "0x4156d3342d5c385a87d264f90653733592000581",
				symbol: "SALT",
				icon: "salt.svg",
				decimal: 8,
				usd_id: "salt"
			}
		}
	};


	var initiateVue = function () {
		Vue.component('v-select', VueSelect.VueSelect);

		function updateConversionRate(_callback) {
			var kyber = CindercloudWeb3.getGlobalWeb3().eth.contract(kyberabi.expected_rate).at(kyberMainnet);

			kyber.getExpectedRate(kyberData.source.address, kyberData.target.address, kyberData.sourceAmount, function (err, result) {
				kyberData.expectedRate = result[0].div(Math.pow(10, kyberData.target.decimal)).toString(10);
				kyberData.slippageRate = result[1].div(Math.pow(10, kyberData.target.decimal)).toString(10);
				_callback();
			});
		}

		function updateBalance(sourceElement) {
			if (sourceElement.symbol === 'ETH') {
				CindercloudWeb3.getGlobalWeb3().eth.getBalance(address, function (err, _balance) {
					kyberData.balance = _balance.div(Math.pow(10, 18)).toString(10);
				});
			} else {
				var erc20 = CindercloudWeb3.getGlobalWeb3().eth.contract(erc20ABI).at(sourceElement.address);
				erc20.decimals(function (_, _decimals) {
					erc20.balanceOf(address, function (_, _balance) {
						kyberData.balance = _balance.div(Math.pow(10, _decimals)).toString(10);
					});
				});
			}
		}

		new Vue({
			el: '#kyberApp',
			data: kyberData,
			methods: {
				updateFromSource: function () {
					if (kyberData.sourceAmount) {
						updateConversionRate(function () {
							kyberData.targetAmount = (kyberData.sourceAmount *  kyberData.slippageRate);
						});
					} else {
						kyberData.targetAmount = 0;
					}
				},
				updateFromTarget: function () {
					if (kyberData.targetAmount) {
						updateConversionRate(function () {
							kyberData.sourceAmount = (kyberData.targetAmount /  kyberData.slippageRate);
						});
					} else {
						kyberData.sourceAmount = 0;
					}
				}
			},
			watch: {
				source: function (val) {
					if (val ===  null || val === 'undefined') {
						this.targets = [];
					} else {
						if (kyberData.source.symbol === 'ETH') {
							this.targets = $.grep(Object.values(kyberData.tokens), function (element) {
								return element.symbol !== 'ETH';
							});
						} else {
							this.targets = [kyberData.tokens.ETH];
						}

						this.target = this.targets[0];
						updateBalance(val);
						updateConversionRate(function () {
							console.log('updated conversion rate');
						});
					}
				}
			},
			created: function () {
				kyberData.source = Object.values(kyberData.tokens)[0];
			}
		});


		$.get('/rest/address/' + address + '/balance', function (_balance) {
			kyberData.sources.push(
				{
					symbol: 'ETH',
					address: 'ethereum_1',
					name: 'Ethereum',
					kyber_address: '0x00eeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeee'
				}
			);
			$.get('/wallet/kyber/' + address + '/sources', function (_tokens) {
				for (var i = 0; i < _tokens.length; i++) {
					kyberData.sources.push(_tokens[i]);
				}
			});
		});
	};

	initiateVue();

	return {
		data: kyberData
	}
})();