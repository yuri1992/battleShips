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
                self.loadGames();
            }, 2000);
            self.renderGames();
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
            });

        },

        onCreateGameClick: function () {
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
                    url: BASE_URL + "/api/games",
                    method: "POST",
                    dataType: "json",
                    data: formData,
                    processData: false,
                    contentType: false,
                    beforeSend: function () {
                        $form.find('.btn-success').attr('disabled', 'disabled');
                        $form.find('.btn-success').append('<span class="glyphicon glyphicon glyphicon-refresh glyphicon-refresh-animate"/>');
                    }
                }).done(function (data) {
                    self.games.unshift(data);
                    self.renderGames();
                    var $modal = $('.modal-body');
                    CommonUtils.clearMessages($modal);
                    self.$modal.modal('hide');
                    $form.get(0).reset();
                }).fail(function (xhr) {
                    var text = '';
                    if (xhr.responseJSON) {
                        text = xhr.responseJSON.desc;
                    }
                    var $modal = $('.modal-body');
                    CommonUtils.clearMessages($modal);
                    CommonUtils.addMessage(text, 'error', $modal)
                }).always(function () {
                    $form.find('.btn-success').removeAttr('disabled');
                    $form.find('.btn-success').find('.glyphicon-refresh-animate').remove();
                })
            }
        },

        loadGames: function () {
            var self = this;
            if (self.isLoadingGames)
                return;

            $.ajax({
                url: BASE_URL + "/api/games",
                method: "GET",
                dataType: "json",
                beforeSend: function () {
                    this.isLoadingGames = true;
                    self.$gamesContainer.find('h1').append($('<span class="glyphicon glyphicon glyphicon-refresh glyphicon-refresh-animate"/>'))
                }
            }).done(function (data) {
                if (!CommonUtils.shallowEqual(self.games, data.matches)) {
                    self.games = data.matches;
                    self.renderGames();
                }
            }).fail(function (xhr, data, status) {
                if (xhr.status == 401) {
                    window.location = BASE_URL + '/pages/signup';
                }
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
                url: BASE_URL + "/api/users",
                method: "GET",
                dataType: "json",
                beforeSend: function () {
                    self.$playersContainer.find('h1').append($('<span class="glyphicon glyphicon glyphicon-refresh glyphicon-refresh-animate"/>'))
                }
            }).done(function (data, text) {
                if (!CommonUtils.shallowEqual(self.users, data.users)) {
                    self.users = data.users;
                    self.renderUsers();
                }
            }).fail(function (xhr, text, status) {
                if (xhr.status == 401) {
                    window.location = BASE_URL + '/pages/signup';
                }
                CommonUtils.addMessage("Error fetching player list, try again later", 'error');
            }).always(function () {
                self.$playersContainer.find('.glyphicon-refresh-animate').remove();
                self.isLoadingPlayers = false;
            })
        },

        joinGame: function (event, matchId) {
            var $btn = $(event.target);
            $.ajax({
                url: BASE_URL + "/api/games/" + matchId + "/register",
                method: "POST",
                dataType: "text",
                beforeSend: function () {
                    $btn.attr('disabled', 'true');
                    $btn.append($('<span class="glyphicon glyphicon glyphicon-refresh glyphicon-refresh-animate"/>'))
                }
            }).done(function () {
                CommonUtils.addMessage("Redirecting you to the game...");
                window.location = window.BASE_URL + '/pages/game';
            }).fail(function (xhr, text, status) {
                console.log(xhr, text, status);
                CommonUtils.clearMessages();
                CommonUtils.addMessage("Error register to endpoint", 'error');
            }).always(function () {
                $btn.removeAttr('disabled');
                $btn.find('.glyphicon-refresh-animate').remove();
            })
        },

        removeGame: function (event, matchId) {
            var self = this;
            var $btn = $(event.target);
            $.ajax({
                url: BASE_URL + "/api/games/" + matchId,
                method: "DELETE",
                dataType: "text",
                beforeSend: function () {
                    $btn.attr('disabled', 'true');
                    $btn.append($('<span class="glyphicon glyphicon glyphicon-refresh glyphicon-refresh-animate"/>'))
                }
            }).done(function () {
                self.loadGames();
                self.CommonUtils.addMessage("Game deleted successfully");

            }).fail(function () {
                CommonUtils.clearMessages();
                CommonUtils.addMessage("Error deleting the games", 'error');
            }).always(function () {
                $btn.removeAttr('disabled');
                $btn.find('.glyphicon-refresh-animate').remove();
            })
        },

        renderGames: function () {
            var self = this;
            this.$gameList.children().remove();

            if (this.games.length === 0) {
                this.$gameList.append("<tr><td colspan='7'>No Games Found.</td></tr>")
            } else {
                var func = {};
                for (var i in this.games) {
                    var $joinGame = $("<button href='#' class='glyphicon glyphicon-play' title='Join the game'></button>");
                    var onJoinGame = function (matchId, e) {
                        console.log(matchId);
                        e.preventDefault();
                        self.joinGame(e, matchId)
                    };
                    $joinGame.click(onJoinGame.bind(this, self.games[i].matchId));

                    var $removeGame = $("<button href='#' class='glyphicon glyphicon-remove' title='Remove the game'></button>");
                    var onRemoveGame = function (matchId, e) {
                        e.preventDefault();
                        self.removeGame(e, matchId);
                    };
                    $removeGame.click(onRemoveGame.bind(this, self.games[i].matchId));

                    var isDeletable = this.games[i].matchStatus == "WAITING_FOR_FIRST_PLAYER";
                    var inProgress = this.games[i].matchStatus == "IN_PROGRESS";
                    var $row = $("<tr>" +
                        "<td>" + this.games[i].matchName + " </td>" +
                        "<td>" + this.games[i].submittingUser.name + " </td>" +
                        "<td>" + this.games[i].gameMode + " </td>" +
                        "<td>" + this.games[i].boardSize + " </td>" +
                        "<td>" + this.games[i].playersCount + " </td>" +
                        "<td>" + (!inProgress ? "<span class='label label-info'>Waiting</span>" : "<span class='label label-success'>Playing</span>") + " </td>" +
                        "<td class='actions'></td>" +
                        "</tr>");

                    $row.find('.actions').append(!inProgress ? $joinGame : '');
                    $row.find('.actions').append(isDeletable && CommonUtils.getCurrentUser().id === this.games[i].submittingUser.id ? $removeGame : '');
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