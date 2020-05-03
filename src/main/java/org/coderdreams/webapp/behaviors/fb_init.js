
const curr_user_id = ${currUserId};

window.fbAsyncInit = function() {
  FB.init({
      appId            : '${fbAppId}',
      autoLogAppEvents : true,
      xfbml            : true,
      version          : 'v6.0'
  });
};