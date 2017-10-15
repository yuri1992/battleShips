$(function ($) {
    function GameList() {
        this.$gameList = $('#js-game-list');
        this.$playerList = $('#js-player-list');
        this.$createBtn = $('#create-game');
        this.$modal = $('#create-game-modal');

        this.games = [];
        this.users = [];

        this.init();
    }

    GameList.prototype = {
        init: function () {
            // Todo: Auto Pooling
            this.loadGames();
            this.loadPlayers();

            var self = this;
            this.$createBtn.click(function (e) {
                self.onCreateGameClick(e);
            });

            this.$modal.find('form').submit(function (e) {
                e.preventDefault();
                self.submitNewGame(e);
            })
        },

        onCreateGameClick: function (event) {
            this.$modal.modal('show');
        },

        submitNewGame: function (event) {
            var $form = $(event.target);
            var formData = new FormData();

            // add assoc key values, this will be posts values
            var file = $form.find('#xml-file').get(0).files[0];
            console.log(file, file.name)
            formData.append("file", file, file.name);
            formData.append("name", $form.find('#name').val());

            $.ajax({
                url: "/api/games",
                method: "POST",
                dataType: "json",
                data: formData,
                processData: false,
                contentType: false,
                beforeSend: function () {
                    $form.find('.btn-success').attr('disabled');
                    $form.find('.btn-success').append('<span class="glyphicon glyphicon glyphicon-refresh glyphicon-refresh-animate"/>');
                }
            }).done(function (data, text) {
                console.log(data);
            }).fail(function (xhr, text, status) {
                console.log('handle errors', xhr, text)
            }).always(function () {
                $form.find('.btn-success').find('.glyphicon-refresh-animate').remove();
            })
        },

        loadGames: function () {
            var self = this;

            $.ajax({
                url: "/api/games",
                method: "GET",
                dataType: "json",
                beforeSend: function () {
                    var $waiting = $('<span class="glyphicon glyphicon glyphicon-refresh glyphicon-refresh-animate"/>');
                    self.$gameList.prepend($waiting);
                }
            }).done(function (data, text) {
                console.log(data);
                self.games = data.matches;
                self.renderGames();
            }).fail(function (xhr, text, status) {
                CommonUtils.addMessage("Error fetching game list, try again later", 'error');
            }).always(function () {
                $('.glyphicon-refresh-animate').remove();
            })
        },

        loadPlayers: function () {
            var self = this;

            $.ajax({
                url: "/api/users",
                method: "GET",
                dataType: "json",
                beforeSend: function () {
                    var $waiting = $('<span class="glyphicon glyphicon glyphicon-refresh glyphicon-refresh-animate"/>');
                    self.$playerList.prepend($waiting);
                },

            }).done(function (data, text) {
                console.log(data);
                self.users = data.users;
                self.renderUsers();
            }).fail(function (xhr, text, status) {
                CommonUtils.addMessage("Error fetching player list, try again later", 'error');
            }).always(function () {
                $('.glyphicon-refresh-animate').remove();
            })
        },

        renderGames: function () {
            this.$gameList.children().remove();

            if (this.games.length === 0) {
                this.$gameList.append("<li class=\"list-group-item\">No Games Found.</li>")
            } else {

            }
        },

        renderUsers: function () {
            this.$playerList.children().remove();

            if (this.users.length === 0) {
                this.$playerList.append("<li class=\"list-group-item\">No Players Found.</li>")
            } else {
                for (var i in this.users) {
                    this.users[i]
                    this.$playerList.append("<li class=\"list-group-item\">" + this.users[i].name + " (" + this.users[i].email + ")" + "</li>")
                }
            }
        }
    };

    (new GameList());
});