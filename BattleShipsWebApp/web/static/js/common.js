$(function () {
    window.CommonUtils = {
        addMessage: function (text, level) {
            var messageHtml = $('<div class="alert" role="alert">' +
                '<button type="button" class="close" data-dismiss="alert" aria-label="Close">\n' +
                '  <span aria-hidden="true">&times;</span>\n' +
                '</button>' + text + '</div>');

            if (level === 'error') {
                messageHtml.addClass('alert-danger');
            } else
                messageHtml.addClass('alert-success');

            $('#js-messages').append(messageHtml);
        },
        clearMessages:function() {
            $('#js-messages').children().remove();
        }
    }
});
