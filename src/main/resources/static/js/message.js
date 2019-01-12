(function (window, document, mui) {
	'use strict';

    //******************************************************************************
    //*                      Message function definitions                             *
    //******************************************************************************
    var
        Message = {

            showMuiErrorMsg : function(as_message, as_title, af_callback) {
                var la_btn = ['OK'],
                    ls_title = as_title || 'Error';
                
                mui.alert(as_message, ls_title, la_btn, af_callback);
            },
                
            showMuiConfirmMsg : function(as_message, as_title, af_callback, af_cancelCallback) {
                var la_btn = ['Yes', 'No'],
                    ls_title = as_title || 'Confirm';
                
                mui.confirm(as_message, ls_title, la_btn, function(ao_event) {
                    if (ao_event.index === 0) {
                        af_callback && af_callback();
                    } else {
                        af_cancelCallback && af_cancelCallback();
                    }
                });
            }
        };

    
	//******************************************************************************
	//*                           Internal Execute Function                        *
	//******************************************************************************
	window.Message = Message;

}(window, document, mui));
