$(function ($) {
    function GameList() {
        this.$gameList = $('#js-game-list');
        this.$playerList = $('#js-player-list');
        this.$createBtn = $('#create-game');

        this.games = [];
        this.users = [];

        this.init();
    }

    GameList.prototype = {
        init: function () {
            this.loadGames();
            this.loadPlayers();

            this.$createBtn.click(function () {
                self.onGameClick();
            })
        },

        onGameClick: function (event) {

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