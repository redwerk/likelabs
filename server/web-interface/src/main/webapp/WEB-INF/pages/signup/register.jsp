<%@include  file="/WEB-INF/pages/commons/header.jsp"%>
<div id="content">
<h1>Sign Up for Like Labs - step two</h1>
                <form action="/signup/end" method="POST">
                    <div>
                        <div class="field">
                            Activation code was sent to your phone. Please enter received code here:
                        </div>
                        <div class="field errorblock">  ${error}</div>
                        <div class="field">
                            <input name="password" type="text" style="width: 180px;"/>
                        </div>
                        <div class="field">
                            <button type="submit" class="btn btn-success save">Register</button>
                        </div>
                    </div>
                </form>
<%@include  file="/WEB-INF/pages/commons/footer.jsp"%>