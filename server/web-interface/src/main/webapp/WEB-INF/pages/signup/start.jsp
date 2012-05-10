<%@include  file="/WEB-INF/common/header.jsp"%>
<div class="row">
    <h4>Sign Up for Like Labs</h4>
        <div class="field errorblock">
            ${notsendsms}
        </div>
    <form action="/signup/register" method="POST" onsubmit="return register();"   >
        <div style="width: 350px;" class="center">
            <div class="field">
                Enter your phone:
            </div>
            <div class="field">
                <select id="countryCodeSelect" style="width: 140px" onchange="document.getElementById('phone').focus();" name="countryCode"></select>
                <input type="text" style="width: 165px;font-family: Arial" id="phone" name="phone"/><div class="field errorblock">${errorphone}</div>
                <script type="text/javascript" src="/static/scripts/phones.js"></script>
                <script type="text/javascript">
                    var select = document.getElementById("countryCodeSelect");
                    var option;
                    for (var i=0; i < codes.length;i++) {
                        option = document.createElement("option");
                        option.innerHTML = codes[i][1];
                        option.value = codes[i][3];
                        select.appendChild(option);
                    }
                    function register() {
                        var chek = document.getElementById("chekTOS");
                        if (chek.checked) {
                            return true;
                        } else {
                            window.alert("You have to agree to our Terms of Service")
                            return false;
                        }
                    }
                </script>
            </div>
            <div class="field" style="font-size: 10px;margin: 0; padding: 0">
                <span><input <c:out value="${checkedtos}"/> id="chekTOS" type="checkbox" name="tos" /></span>
                <span>I agree to the <a href="/tos" target="_blank">Terms of Service</a></span>
            </div>
            <div class="field">
                <input  type="submit" value="Sign Up" style="width: 187px;"/>
            </div>
        </div>
    </form>
</div>
<%@include  file="/WEB-INF/common/footer.jsp"%>