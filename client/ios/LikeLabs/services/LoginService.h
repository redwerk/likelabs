#import <Foundation/Foundation.h>
#import "ASIHTTPRequest.h"
#import "SettingsDao.h"
#import "SettingsService.h"

@interface LoginService : NSObject

@property (nonatomic, retain) SettingsDao* dao;
@property (nonatomic,retain) SettingsService* settingsService;

- (BOOL)checkLogin:(NSString*)login andPassword:(NSString*)password error:(NSError**) error;
- (BOOL)checkLogout:(NSString*)login andPassword:(NSString*)password error:(NSError**) error;

@end
