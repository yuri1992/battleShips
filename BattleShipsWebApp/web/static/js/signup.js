$(function ($) {
    function Signup() {
        this.$form = $('#js-login-form');

        this.$username = this.$form.find('#username');
        this.$usernameGroup = this.$form.find('#form-group-username');

        this.$email = this.$form.find('#email');
        this.$emailGroup = this.$form.find('#form-group-email');

        this.$password = this.$form.find('#password');
        this.$passwordGroup = this.$form.find('#form-group-password');

        if (this.$form.length === 0) {
            console.log("Login form not found");
        }

        this.init();
    }

    Signup.prototype = {
        init: function () {
            var f = this.onSubmit.bind(this);
            this.$form.submit(f);
        },

        addMessage: function (text, level) {
            var messageHtml = $('<div class="alert" role="alert">' +
                '<button type="button" class="close" data-dismiss="alert" aria-label="Close">\n' +
                '  <span aria-hidden="true">&times;</span>\n' +
                '</button>' + text + '</div>');

            if (level === 'error') {
                messageHtml.addClass('alert-danger');
            } else
                messageHtml.addClass('alert-success');

            $('.js-messages').append(messageHtml);
        },

        isEmailValid: function () {
            return /^(([^<>()\[\]\.,;:\s@\"]+(\.[^<>()\[\]\.,;:\s@\"]+)*)|(\".+\"))@(([^<>()[\]\.,;:\s@\"]+\.)+[^<>()[\]\.,;:\s@\"]{2,})$/i.test(this.$email.val());
        },

        validateEmail: function () {
            if (this.isEmailValid()) {
                this.$emailGroup.addClass('has-success');
            } else {
                this.$emailGroup.addClass('has-danger');
                this.$emailGroup.append('<p class="help-block ">Email Invalid, Please enter valid email</p>')
            }
        },

        isPasswordValid: function () {
            return this.$password.val().length > 5;
        },

        validatePassword: function () {
            this.$passwordGroup.remove('has-success').remove('has-error');
            if (this.isPasswordValid()) {
                this.$passwordGroup.addClass('has-success');
            } else {
                this.$passwordGroup.addClass('has-error');
                this.$passwordGroup.append('<p class="help-block ">Password too short, it should be at least 5 chars</p>')
            }
        },


        isNameValid: function () {
            return this.$username.val().length > 2;
        },

        validateName: function () {
            this.$passwordGroup.remove('has-success').remove('has-error');
            if (this.isPasswordValid()) {
                this.$passwordGroup.addClass('has-success');
            } else {
                this.$passwordGroup.addClass('has-error');
                this.$passwordGroup.append('<p class="help-block ">Name is too short, should be at least 2 chars</p>')
            }
        },

        onSubmit: function (e) {
            e.preventDefault();
            this.$form.find('.help-block').remove();

            if (this.isPasswordValid() && this.isEmailValid() && this.isNameValid()) {
                var self = this;

                $.ajax({
                    method: "POST",
                    data: {
                        username: this.$username.val(),
                        email: this.$email.val(),
                        password: this.$password.val()
                    },
                    dataType: "json",
                    beforeSend: function () {
                        var btn = self.$form.find('button');
                        btn.attr('disabled');
                        btn.append('<span class="glyphicon-refresh-animate"/>');
                        $('.js-messages .alert').remove();
                    },
                    always: function () {
                        self.$form.find('button').removeAttr('disabled');
                    }
                }).done(function (data, text) {
                    self.addMessage("Welcome, you will be redirected in a few seconds...");
                    setTimeout(function() {
                        window.location = 'pages/profile.html';
                    }, 2000);
                }).fail(function (xhr, text, status) {
                    self.addMessage("Email and Password didn't match, please try again", 'error');
                })

            } else {
                this.validateName();
                this.validateEmail();
                this.validatePassword();
            }
            return false;
        }
    };

    (new Signup());
});