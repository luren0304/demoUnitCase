$(function (){
	'use strict';

    //******************************************************************************
    //*   Variable definitions(include external reference and internal variable)   *
    //******************************************************************************

	var
        /********************* External reference ***************************/
        Constants = window.Constants,

        web = window.web,

        /********************* Internal static variable *********************/
        VS_ID_LOGIN_FORM = '#login-form',
        VS_ID_LOGIN_USER = '#app-login-username',
        VS_ID_LOGIN_PWD = '#app-login-password',
        VS_ID_LOGIN_SUBMIT = '#app-login-submit',
        VS_ID_LOGIN_ERR = '#app_login_error',
        VS_ID_LOGIN_MSG = '#app_error_msg',

        VS_MSG_EMPTY_NAME = 'Username cannot be empty!',
        VS_MSG_EMPTY_PWD = 'Password cannot be empty!',

       /********************* Internal variable ****************************/
       vs_urlProjectPath,
       vo_$error = null,
       vo_$errorMsg = null,
       vo_errorMsgData = {};

    //******************************************************************************
    //*                           Private function definitions                     *
    //******************************************************************************

    function _setProjectPath() {
        var l_location = document.location,
            ls_pathName = l_location ? l_location.pathname : '',
            ls_projectPath = ls_pathName.substring(1);

        vs_urlProjectPath = '/' + ls_projectPath.substring(0, ls_projectPath.indexOf('/'));
    }

    function _initErrorMsg() {
        vo_$error = $(VS_ID_LOGIN_ERR);
        vo_$errorMsg = $(VS_ID_LOGIN_MSG);

        vo_errorMsgData = {
            msg: '',
            field: null
        };
    }

    function _setErrorMsg(ao_$field, as_message) {
        vo_errorMsgData = {
            msg: as_message,
            field: ao_$field
        };
    }

    function _hideErrorMsg() {
        vo_$error.addClass('mui-hidden');
        vo_$error.fadeOut('fast');
    }

    function _showErrorMsg() {
        if ($.trim(vo_errorMsgData.msg).length > 0) {
            if (vo_errorMsgData.field) {
                vo_errorMsgData.field.focus();
            }

            vo_$errorMsg.html(vo_errorMsgData.msg);

            vo_$error.removeClass('mui-hidden');
        }
    }

    function _validate() {
        var lo_$userName = $(VS_ID_LOGIN_USER),
            lo_$password = $(VS_ID_LOGIN_PWD),
            ls_userName = lo_$userName.val(),
            ls_password = lo_$password.val();

        if (ls_userName.trim().length < 1) {
            _setErrorMsg(lo_$userName, VS_MSG_EMPTY_NAME);

            return false;
        }

        if (ls_password.trim().length < 1) {
            _setErrorMsg(lo_$password, VS_MSG_EMPTY_PWD);

            return false;
        }

        return true;
    }

    //******************************************************************************
    //*                           Public function definitions                      *
    //******************************************************************************
    /**
     * Login constructor function
     *
     * @constructor
     */
    function Login(){
        _setProjectPath();

        _initErrorMsg();
    }

    /**
     * Login prototype
     */
    Login.prototype = {
        constructor: Login,

        submit: function(ao_event) {
            $(this).type = 'POST';
            $(this).url = vs_urlProjectPath + Config.CS_URL_APP_LOGIN;

            if (_validate()) {
                _hideErrorMsg();

                return true;
            } else {
                _showErrorMsg();

                return false;
            }
        }
    };

    //******************************************************************************
    //*                           Internal Execute Function                        *
    //******************************************************************************
    (function main(){
        var lo_login = new Login(),
            lo_$loginForm = $(VS_ID_LOGIN_FORM),
            lo_$loginSubmit = $(VS_ID_LOGIN_SUBMIT);

        mui.init();

        lo_$loginForm.submit(lo_login.submit);

        lo_$loginSubmit.on('tap', function () {
            lo_$loginForm.submit();
        });

        lo_$loginForm.keyup(function(ao_event) {
            if (ao_event.keyCode === Constants.CI_KEY_ENTER) {
                lo_$loginSubmit.trigger('tap');
            }
        });

        $(VS_ID_LOGIN_USER + ',' + VS_ID_LOGIN_PWD).keyup(function(ao_event){
            if ($.trim(ao_event.target.value).length > 0) {
                _hideErrorMsg();
            }
        });

        mui.back = function() {
            window.close();
        };
    }());
});
