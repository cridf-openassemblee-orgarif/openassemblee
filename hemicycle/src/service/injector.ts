import HttpService from './HttpService';
import EventBus from './EventBus';
import { DataService } from '../component/util/DataService';
import { ArchiveService } from '../component/util/ArchiveService';

class Injector {
    public static instance: Injector;

    public urlBase = (global.urlBase ? global.urlBase : '') + '/api/';

    public httpService = new HttpService();

    public dataService = new DataService();

    public archiveService = new ArchiveService();

    applicationEventBus = new EventBus<ApplicationEvents>();
}

export const injector = () => {
    if (!Injector.instance) {
        Injector.instance = new Injector();
    }
    return Injector.instance;
};
