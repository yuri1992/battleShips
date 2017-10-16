$(function ($) {
    function GameList() {
        this.$gameList = $('#js-game-list');
        this.$playerList = $('#js-player-list');
        this.$playersContainer = $('.players-container');
        this.$gamesContainer = $('.games-container');
        this.$createBtn = $('#create-game');
        this.$modal = $('#create-game-modal');

        this.games = [];
        this.users = [];

        this.isLoadingGames = false;
        this.isLoadingPlayers = false;

        this.init();
    }

    GameList.prototype = {
        init: function () {
            var self = this;

            this._intervalGames = setInterval(function () {
                if (!self.isLoadingGames)
                    self.loadGames();
            }, 2000);
            this.loadGames();

            this._intervalPlayer = setInterval(function () {
                if (!self.isLoadingPlayers)
                    self.loadPlayers();
            }, 2000);
            this.loadPlayers();


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
        validateCreateGameForm: function () {
            return true;
        },

        submitNewGame: function (event) {
            var $form = $(event.target);
            var self = this;
            if (this.validateCreateGameForm()) {
                var formData = new FormData();
                // add assoc key values, this will be posts values
                var file = $form.find('#xml-file').get(0).files[0];
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
                        $form.find('.btn-success').attr('disabled', 'disabled');
                        $form.find('.btn-success').append('<span class="glyphicon glyphicon glyphicon-refresh glyphicon-refresh-animate"/>');
                    }
                }).done(function (data, text) {
                    self.games.push(data);
                    self.renderGames();
                    self.$modal.modal('hide');
                }).fail(function (xhr, error, status) {
                    var text = '';
                    if (xhr.responseJSON) {
                        text = xhr.responseJSON.desc;
                    }
                    var $modal = $('.modal-body');
                    CommonUtils.clearMessages($modal);
                    CommonUtils.addMessage("Error Uploading Xml File: " + text, 'error', $modal)
                }).always(function () {
                    $form.find('.btn-success').removeAttr('disabled');
                    $form.find('.btn-success').find('.glyphicon-refresh-animate').remove();
                })
            }
        },

        loadGames: function () {
            var self = this;
            this.isLoadingGames = true;
            $.ajax({
                url: "/api/games",
                method: "GET",
                dataType: "json",
                beforeSend: function () {
                    self.$gamesContainer.find('h1').append($('<span class="glyphicon glyphicon glyphicon-refresh glyphicon-refresh-animate"/>'))
                }
            }).done(function (data, text) {
                console.log(data);
                self.games = data.matches;
                self.renderGames();
            }).fail(function (xhr, text, status) {
                CommonUtils.addMessage("Error fetching game list, try again later", 'error');
            }).always(function () {
                self.$gamesContainer.find('.glyphicon-refresh-animate').remove();
                self.isLoadingGames = false;
            })
        },

        loadPlayers: function () {
            var self = this;
            this.isLoadingPlayers = true;
            $.ajax({
                url: "/api/users",
                method: "GET",
                dataType: "json",
                beforeSend: function () {
                    self.$playersContainer.find('h1').append($('<span class="glyphicon glyphicon glyphicon-refresh glyphicon-refresh-animate"/>'))
                }
            }).done(function (data, text) {
                console.log(data);
                self.users = data.users;
                self.renderUsers();
            }).fail(function (xhr, text, status) {
                CommonUtils.addMessage("Error fetching player list, try again later", 'error');
            }).always(function () {
                self.$playersContainer.find('.glyphicon-refresh-animate').remove();
                self.isLoadingPlayers = false;
            })
        },

        joinGame: function (matchId) {
            console.log('Joing game ' + matchId);
        },

        renderGames: function () {
            var self = this;
            this.$gameList.children().remove();

            if (this.games.length === 0) {
                this.$gameList.append("<tr><td colspan='8'>No Games Found.</td></tr>")
            } else {
                for (var i in this.games) {
                    var $joinGame = $("<a href='#' class='glyphicon glyphicon-play'></a>");
                    $joinGame.click(function (e) {
                        e.preventDefault();
                        self.joinGame(self.games[i].matchId)
                    });

                    var isWaitingPlayers = this.games[i].gameManager.state === "LOADED";
                    var $row = $("<tr>" +
                        "<td>" + this.games[i].matchId + " </td>" +
                        "<td>" + this.games[i].matchName + " </td>" +
                        "<td>" + this.games[i].submittingUser.name + " </td>" +
                        "<td>" + this.games[i].gameManager.mode + " </td>" +
                        "<td>" + this.games[i].gameManager.boardSize + " </td>" +
                        "<td>" + 0 + " </td>" +
                        "<td>" + (isWaitingPlayers ? "<span class='label label-info'>Waiting</span>" : "<span class='label label-success'>Playing</span>") + " </td>" +
                        "<td class='actions'></td>" +
                        "</tr>");

                    $row.find('.actions').append(isWaitingPlayers ? $joinGame : '');
                    this.$gameList.append($row);
                }
            }
        },

        renderUsers: function () {
            this.$playerList.children().remove();

            if (this.users.length === 0) {
                this.$gameList.append("<tr><td colspan='2'>No Players Found</td></tr>")
            } else {
                for (var i in this.users) {
                    this.$playerList.append("<tr>" +
                        "<td>" + this.users[i].name + "</td>" +
                        "<td>" + this.users[i].email + "</td>" +
                        "</tr>")
                }
            }
        }
    };

    (new GameList());
});