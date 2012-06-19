<%@include  file="/WEB-INF/pages/commons/header.jsp"%>
    <div id="content">
    
    <h1>Sign Up for Like Labs</h1> 

                <div class="field errorblock">
                    ${notsendsms}
                </div>

                <form action="/signup/register" method="POST" onsubmit="return register();"   >
                    <div style="width: 350px;" class="center">
                        <div class="field">
                            Enter your phone:
                        </div>
                        <div style="margin: 5px 0 15px;">
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
                            <div class="field" style="font-size: 12px !important;" >
                             <input <c:out value="${checkedtos}"/> id="chekTOS" type="checkbox" name="tos" style="margin: 0; vertical-align: middle;" /> 
                            <label for="chekTOS" style="vertical-align: middle;">
                               
                                I agree to the
                            </label> <a href="/tos" target="_blank" style="vertical-align: middle; padding-top: 1px; font-weight: bold;" >Terms of Service</a>
                        </div>
                        <div class="field">
                            <button type="submit" class="btn btn-success save">Sign Up</button>
                        </div>
                    </div>
                </form>
    </div>
<%@include  file="/WEB-INF/pages/commons/footer.jsp"%>