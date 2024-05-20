import { ICandidate } from 'app/entities/candidate/candidate.model';

export interface IResume {
  id: number;
  file?: string | null;
  fileContentType?: string | null;
  filename?: string | null;
  owner?: ICandidate | null;
}

export type NewResume = Omit<IResume, 'id'> & { id: null };
