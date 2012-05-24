#import "SettingsService.h"
#import "Review.h"
#import "User.h"
#import "URLUtil.h"
#import "SettingsDao.h"

@implementation SettingsService

static NSUInteger DEFAULT_TABLET_ID = 1;

- (void)getSettings {    
    ASIHTTPRequest *request = [[ASIHTTPRequest alloc] initWithURL:[URLUtil getSettingsUrlForTablet:DEFAULT_TABLET_ID]];
    [request setDelegate:self];
    [request startAsynchronous];
    
    //TODO settingsDao.setSettings();
}

- (void)requestFinished:(ASIHTTPRequest *)request {
    NSLog(@"Response status: %d", request.responseStatusCode);
    NSLog(@"Response string: %@", request.responseString);
    
}

- (void)requestFailed:(ASIHTTPRequest *)request {
    NSLog(@"Error: %@", request.error);
}

@end
