var Kyber = (function () {

	var kyberData = {
		sources: [],
		source: null,
		target: null,
		targetAmount: 0,
		sourceAmount: 0
	};

	var initiateVue = function () {
		Vue.component('v-select', VueSelect.VueSelect);

		new Vue({
			el: '#kyberApp',
			data: kyberData,
			methods: {},
			computed: {
				availableTargets: function () {
					return [];
				}
			}
		});

		var address = $('#currentAddress').val();
		$.get('/rest/address/' + address + '/balance', function (_balance) {
			kyberData.sources.push(
				{
					symbol: 'ETH',
					address: 'ethereum_1',
					name: 'Ethereum',
					kyber_address: '0x00eeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeee'
				}
			);
			kyberData.source = kyberData.sources[0];
			$.get('/wallet/kyber/' + address + '/sources', function (_tokens) {
				for (var i = 0; i < _tokens.length; i++) {
					kyberData.sources.push(_tokens[i]);
				}
			});
		});
	};

	initiateVue();

	return {}
})();