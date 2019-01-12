(function (window, document, $, mui) {
	'use strict';

	//******************************************************************************
	//*   Variable definitions(include external reference and internal variable)   *
	//******************************************************************************

	var
	/********************* External reference ***************************/
	Config = window.Config,

    vs_urlProjectPath = '';

	//******************************************************************************
	//*                           Private function definitions                     *
	//******************************************************************************
    function _setProjectPath() {
        var l_location = document.location,
            ls_pathName = l_location ? l_location.pathname : '',
            ls_projectPath = ls_pathName.substring(1);

        vs_urlProjectPath = '/' + ls_projectPath.substring(0, ls_projectPath.indexOf('/'));
    }
	/**
	 * Send ajax request with 'json', if not set success/error/failure callback function, will call the default function
	 *
	 * @param {String} as_url
	 * @param {Object} ao_data
	 * @param {boolean} ab_sync
	 * @param {Function} [af_success]
	 * @param {Function} [af_error]
	 * @param {Function} [af_failure]
	 *
	 * @private
	 */
	function _sendAjaxRequest(as_url, ao_data, ab_sync, af_success, af_error, af_failure) {
		$.ajax({
			type: 'POST',
			cache: true,
			url: as_url,
			dataType: 'json',
			contentType: 'application/json',
			data: ao_data ? JSON.stringify(ao_data) : undefined,
            beforeSend: function (a_request) {
                _requestLoading(true);
            },
			success: function (result) {
                if (af_success) {
                    af_success(result);
                }
			},
			error: function (a_xmlHttpRequest, a_textStatus, a_errorThrown) {
				if (af_error) {
					af_error(a_xmlHttpRequest, a_textStatus, a_errorThrown);
				}
			},
            complete : function(a_xmlHttpRequest, a_textStatus) {
               _requestLoading();
            }
		});
	}
  
	/**
	 * Set request full path and then send request to web
	 *
	 * @param {String} as_url
	 * @param {Object} ao_data
	 * @param {boolean} ab_sync
	 * @param {Function} [af_success]
	 * @param {Function} [af_error]
	 * @param {Function} [af_failure]
	 *
	 * @private
	 */
	function _sendRequest(as_url, ao_data, ab_sync, af_success, af_error, af_failure) {
		var lf_successCallback,
            lf_errorCallback,
            lf_failureCallback;

		if (af_success) {
			lf_successCallback = af_success;
		} else {
			lf_successCallback = _successCallback;
		}

		if (af_error) {
			lf_errorCallback = af_error;
		} else {
			lf_errorCallback = _errorCallback;
		}

		if (af_failure) {
			lf_failureCallback = af_failure;
		} else {
			lf_failureCallback = _failureCallback;
		}

		_sendAjaxRequest(vs_urlProjectPath + as_url, ao_data, ab_sync, lf_successCallback, lf_errorCallback, lf_failureCallback);
	}

	/**
	 * Default response success callback
	 *
	 * @param ao_result
	 *
	 * @private
	 */
	function _successCallback(ao_result) {
		// To do something
	}

	/**
	 * Default response failure callback
	 *
	 * @param ao_result
	 *
	 * @private
	 */
	function _failureCallback(ao_result) {
		var ls_errorCode = ao_result.code || '',
            ls_errorMessage = ao_result.message || '';

		Message.showMuiErrorMsg(ls_errorMessage, "Failure");
	}

	/**
	 * Default response error callback
	 *
	 * @param ao_xmlHttpRequest
	 * @param ao_textStatus
	 * @param ao_errorThrown
	 *
	 * @private
	 */
	function _errorCallback(ao_xmlHttpRequest, ao_textStatus, ao_errorThrown) {
        var li_status = ao_xmlHttpRequest.status,
            ls_statusText = ao_xmlHttpRequest.statusText,
            ls_responesText = ao_xmlHttpRequest.responseText;
        
        if (ao_textStatus === 'abort') {
            return;
        }

        Message.showMuiErrorMsg(li_status + ' ' + ls_statusText);
	}

    function _requestLoading(ab_show, ab_abort) {
        if (ab_abort) {
            _hideLoading();
        } else {
            if (ab_show) {
                _showLoading();
            } else {
                _hideLoading();
            }
        }
    }

    function _hideLoading() {
        mui.hideLoading();
        
    }
    
    function _showLoading() {
        mui.showLoading("Loading...", "div");
    }

	//******************************************************************************
	//*                           Public function definitions                      *
	//******************************************************************************
	/**
	 * Web constructor function
	 *
	 * @constructor
	 */
	function Web() {
        _setProjectPath();
	}

	/**
	 * Web prototype
	 */
    Web.prototype = {
		constructor : Web,

        showLoading : function() {
            _showLoading();
        },
        
        hideLoading : function() {
            _hideLoading();
        },

		/**
		 * Send asynchronous request
		 *
		 * @param {String} as_url
		 * @param {Object} ao_data
		 * @param {Function} [af_success]
		 * @param {Function} [af_error]
		 * @param {Function} [af_failure]
		 */
		sendRequest: function (as_url, ao_data, af_success, af_error, af_failure) {
			_sendRequest(as_url, ao_data, false, af_success, af_error, af_failure);
		},

        forcedBack: function() {
            _requestLoading(false, true);
                        
            mui.back();
        },
        
        delayCall : function(af_callback, ai_delay) {
            var li_delayTime = ai_delay || 50;
                        
            af_callback && mui.later(af_callback, li_delayTime);
        },

        authLogout : function (af_successCallback) {
            this.sendRequest(Config.CS_URL_APP_LOGOUT, null, af_successCallback);
        }
	};

	//******************************************************************************
	//*                           Internal Execute Function                        *
	//******************************************************************************
	(function main(){
        window.web = new Web();
    })();
}(window, document, jQuery, mui));
